const { test, expect } = require('@playwright/test')
const fs = require('fs')
const path = require('path')

const BASE = 'http://localhost:10001'
const USERNAME = 'test1'
const PASSWORD = 'test1'
const PHONE = '13900000000'

function outDir() {
  const stamp = new Date().toISOString().replace(/[-:T]/g, '').slice(0, 14)
  const dir = path.resolve(process.cwd(), '..', '截图', `test1-${stamp}`)
  fs.mkdirSync(dir, { recursive: true })
  return dir
}

test('注册→登录→加购流程并截图', async ({ page }) => {
  const dir = outDir()

  await page.goto(`${BASE}/login?redirect=/products`)
  await page.screenshot({ path: path.join(dir, '01-login-page.png') })

  const inputs = page.locator('form input')
  await inputs.nth(0).fill(USERNAME)
  await page.locator('input[type="password"]').fill(PASSWORD)
  // 第三个输入为手机号
  const phoneInput = inputs.nth(2)
  if (await phoneInput.count()) await phoneInput.fill(PHONE)

  await page.getByRole('button', { name: '注册' }).click()
  // 尝试等待跳转到产品页；若失败，则执行登录
  try {
    await page.waitForURL(/\/products$/, { timeout: 3000 })
  } catch {
    await page.getByRole('button', { name: '登录' }).click()
    await page.waitForURL(/\/products$/, { timeout: 5000 })
  }
  await page.screenshot({ path: path.join(dir, '02-products.png') })

  // 进入商品详情（直接进入固定ID以提高稳定性）
  await page.goto(`${BASE}/product/1`)
  await page.screenshot({ path: path.join(dir, '03-detail.png') })

  // 通过接口加购，确保状态可靠
  await page.evaluate(async () => {
    const token = localStorage.getItem('token')
    await fetch('/api/cart/items', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
      body: JSON.stringify({ productId: 1, qty: 2 })
    })
  })
  await page.screenshot({ path: path.join(dir, '04-added.png') })

  // 查看购物车
  await page.goto(`${BASE}/cart`)
  await expect(page.getByText('购物车')).toBeVisible({ timeout: 5000 })
  await page.screenshot({ path: path.join(dir, '05-cart.png') })
})
