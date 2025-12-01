function jget(k, d) {
  var v = localStorage.getItem(k)
  return v ? JSON.parse(v) : d
}

function jset(k, v) {
  localStorage.setItem(k, JSON.stringify(v))
}

var seed = {
  cats: jget('demo_cats', null),
  prods: jget('demo_prods', null),
  users: jget('demo_users', null),
  addrs: jget('demo_addrs', null),
  orders: jget('demo_orders', null),
  auth: jget('demo_auth', null)
}

if (!seed.cats) {
  seed.cats = [
    { id: 1, name: '手机' },
    { id: 2, name: '电脑' },
    { id: 3, name: '家电' },
    { id: 4, name: '周边' }
  ]
  jset('demo_cats', seed.cats)
}

if (!seed.prods) {
  seed.prods = [
    {
      id: 1,
      name: '旗舰手机 X',
      categoryId: 1,
      price: 3999,
      stock: 128,
      desc: '高性能处理器，高清屏幕',
      images: ['pd1', 'pd2'],
      sales: 120
    },
    {
      id: 2,
      name: '轻薄笔记本 Pro',
      categoryId: 2,
      price: 5999,
      stock: 64,
      desc: '轻薄高性能，长续航',
      images: ['nb1', 'nb2'],
      sales: 80
    },
    {
      id: 3,
      name: '智能电视 4K',
      categoryId: 3,
      price: 2999,
      stock: 50,
      desc: '4K HDR 大屏',
      images: ['tv1'],
      sales: 60
    },
    {
      id: 4,
      name: '降噪耳机',
      categoryId: 4,
      price: 999,
      stock: 200,
      desc: '主动降噪，舒适佩戴',
      images: ['hp1'],
      sales: 150
    },
    {
      id: 5,
      name: '游戏手机 Z',
      categoryId: 1,
      price: 4999,
      stock: 40,
      desc: '高帧率游戏体验',
      images: ['pd3'],
      sales: 70
    },
    {
      id: 6,
      name: '台式主机 R',
      categoryId: 2,
      price: 7999,
      stock: 20,
      desc: '高端发烧级配置',
      images: ['pc1'],
      sales: 30
    },
    {
      id: 7,
      name: '变频空调',
      categoryId: 3,
      price: 2599,
      stock: 35,
      desc: '节能静音',
      images: ['ac1'],
      sales: 45
    },
    {
      id: 8,
      name: '机械键盘',
      categoryId: 4,
      price: 599,
      stock: 300,
      desc: '青轴手感',
      images: ['kb1'],
      sales: 200
    }
  ]
  jset('demo_prods', seed.prods)
}

if (!seed.users) {
  seed.users = [
    {
      id: 1,
      username: 'admin',
      role: 'admin',
      phone: '',
      status: 'enabled',
      createdAt: '2025-01-01'
    },
    {
      id: 2,
      username: 'customer',
      role: 'user',
      phone: '13800000000',
      status: 'enabled',
      createdAt: '2025-01-02'
    }
  ]
  jset('demo_users', seed.users)
}

if (!seed.addrs) {
  seed.addrs = [
    {
      id: 1,
      userId: 2,
      name: '张三',
      phone: '13800000000',
      detail: '北京市朝阳区',
      isDefault: true
    },
    {
      id: 2,
      userId: 2,
      name: '李四',
      phone: '13900000000',
      detail: '上海市浦东新区',
      isDefault: false
    }
  ]
  jset('demo_addrs', seed.addrs)
}

if (!seed.orders) {
  seed.orders = []
  jset('demo_orders', seed.orders)
}

if (!seed.auth) {
  seed.auth = { userId: null }
  jset('demo_auth', seed.auth)
}

function cartKey(uid) {
  return 'demo_cart_' + uid
}

var store = {
  cats: function () {
    return jget('demo_cats', [])
  },
  prods: function () {
    return jget('demo_prods', [])
  },
  users: function () {
    return jget('demo_users', [])
  },
  addrs: function () {
    return jget('demo_addrs', [])
  },
  orders: function () {
    return jget('demo_orders', [])
  },
  auth: function () {
    return jget('demo_auth', { userId: null })
  },

  login: function (u) {
    var us = this.users()
    var f = us.find(function (x) { return x.username === u })
    if (!f) { return false }
    jset('demo_auth', { userId: f.id })
    return true
  },
  register: function (u, p, ph) {
    var us = this.users()
    var id = us.length
      ? Math.max.apply(null, us.map(function (x) { return x.id })) + 1
      : 1
    var nu = {
      id: id,
      username: u,
      role: 'user',
      phone: ph || '',
      status: 'enabled',
      createdAt: new Date().toISOString().slice(0, 10)
    }
    us.push(nu)
    jset('demo_users', us)
    jset('demo_auth', { userId: id })
    return nu
  },
  logout: function () {
    jset('demo_auth', { userId: null })
  },
  currentUser: function () {
    var a = this.auth()
    if (!a.userId) { return null }
    return this.users().find(function (x) { return x.id === a.userId }) || null
  },

  hot: function (n) {
    return this.prods()
      .slice()
      .sort(function (a, b) { return b.sales - a.sales })
      .slice(0, n || 8)
  },
  getProduct: function (id) {
    return this.prods().find(function (p) { return p.id == id })
  },
  getProducts: function (q) {
    var arr = this.prods().slice()
    if (q && q.categoryId) {
      arr = arr.filter(function (p) { return p.categoryId == q.categoryId })
    }
    if (q && q.keyword) {
      arr = arr.filter(function (p) {
        return p.name.indexOf(q.keyword) >= 0
      })
    }
    if (q && q.minPrice) {
      arr = arr.filter(function (p) { return p.price >= q.minPrice })
    }
    if (q && q.maxPrice) {
      arr = arr.filter(function (p) { return p.price <= q.maxPrice })
    }
    if (q && q.sort) {
      if (q.sort === 'price_asc') {
        arr.sort(function (a, b) { return a.price - b.price })
      } else if (q.sort === 'price_desc') {
        arr.sort(function (a, b) { return b.price - a.price })
      } else if (q.sort === 'sales') {
        arr.sort(function (a, b) { return b.sales - a.sales })
      }
    }
    var page = (q && q.page) || 1
    var size = (q && q.size) || 8
    var total = arr.length
    var list = arr.slice((page - 1) * size, (page - 1) * size + size)
    return { list: list, total: total, page: page, size: size }
  },

  getCart: function () {
    var u = this.currentUser()
    if (!u) { return [] }
    return jget(cartKey(u.id), [])
  },
  setCart: function (items) {
    var u = this.currentUser()
    if (!u) { return }
    jset(cartKey(u.id), items)
  },
  addToCart: function (pid, qty) {
    var items = this.getCart()
    var it = items.find(function (i) { return i.productId == pid })
    if (it) { it.qty += qty }
    else { items.push({ productId: pid, qty: qty }) }
    this.setCart(items)
  },
  setCartQty: function (pid, qty) {
    var items = this.getCart()
    var it = items.find(function (i) { return i.productId == pid })
    if (it) { it.qty = qty; this.setCart(items) }
  },
  removeCartItem: function (pid) {
    var items = this.getCart()
      .filter(function (i) { return i.productId != pid })
    this.setCart(items)
  },
  clearCart: function () {
    this.setCart([])
  },

  getUserAddrs: function (uid) {
    return this.addrs().filter(function (a) { return a.userId == uid })
  },
  addAddr: function (uid, addr) {
    var arr = this.addrs()
    var id = arr.length
      ? Math.max.apply(null, arr.map(function (x) { return x.id })) + 1
      : 1
    addr.id = id
    addr.userId = uid
    arr.push(addr)
    jset('demo_addrs', arr)
    return addr
  },
  updateAddr: function (addr) {
    var arr = this.addrs()
    var i = arr.findIndex(function (a) { return a.id == addr.id })
    if (i >= 0) { arr[i] = addr; jset('demo_addrs', arr) }
  },
  deleteAddr: function (id) {
    var arr = this.addrs()
      .filter(function (a) { return a.id != id })
    jset('demo_addrs', arr)
  },

  createOrder: function (addrId) {
    var u = this.currentUser()
    if (!u) { return null }
    var items = this.getCart()
    var prods = this.prods()
    var enriched = items.map(function (i) {
      var p = prods.find(function (pp) { return pp.id == i.productId })
      return {
        productId: i.productId,
        name: p ? p.name : '',
        price: p ? p.price : 0,
        qty: i.qty
      }
    })
    var amount = enriched
      .reduce(function (s, it) { return s + it.price * it.qty }, 0)
    var id = this.orders().length
      ? Math.max.apply(
          null,
          this.orders().map(function (o) { return o.id })
        ) + 1
      : 1000
    var addr = this.addrs().find(function (a) { return a.id == addrId })
    var o = {
      id: id,
      userId: u.id,
      items: enriched,
      address: addr,
      amount: amount,
      status: '已创建',
      createdAt: new Date().toISOString()
    }
    var arr = this.orders()
    arr.push(o)
    jset('demo_orders', arr)
    this.clearCart()
    return o
  },
  getOrder: function (id) {
    return this.orders().find(function (o) { return o.id == id })
  },
  getUserOrders: function (uid) {
    return this.orders().filter(function (o) { return o.userId == uid })
  },
  updateOrderStatus: function (id, st) {
    var arr = this.orders()
    var i = arr.findIndex(function (o) { return o.id == id })
    if (i >= 0) { arr[i].status = st; jset('demo_orders', arr) }
  },

  toggleUserStatus: function (id) {
    var us = this.users()
    var i = us.findIndex(function (u) { return u.id == id })
    if (i >= 0) {
      us[i].status = us[i].status === 'enabled' ? 'disabled' : 'enabled'
      jset('demo_users', us)
    }
  },

  createCategory: function (cat) {
    var arr = this.cats()
    var id = arr.length
      ? Math.max.apply(null, arr.map(function (x) { return x.id })) + 1
      : 1
    arr.push({ id: id, name: cat.name })
    jset('demo_cats', arr)
  },
  updateCategory: function (cat) {
    var arr = this.cats()
    var i = arr.findIndex(function (c) { return c.id == cat.id })
    if (i >= 0) { arr[i] = cat; jset('demo_cats', arr) }
  },
  deleteCategory: function (id) {
    var arr = this.cats()
      .filter(function (c) { return c.id != id })
    jset('demo_cats', arr)
  },

  createProduct: function (p) {
    var arr = this.prods()
    var id = arr.length
      ? Math.max.apply(null, arr.map(function (x) { return x.id })) + 1
      : 1
    p.id = id
    p.images = p.images || []
    p.sales = p.sales || 0
    arr.push(p)
    jset('demo_prods', arr)
  },
  updateProduct: function (p) {
    var arr = this.prods()
    var i = arr.findIndex(function (x) { return x.id == p.id })
    if (i >= 0) { arr[i] = p; jset('demo_prods', arr) }
  },
  deleteProduct: function (id) {
    var arr = this.prods()
      .filter(function (x) { return x.id != id })
    jset('demo_prods', arr)
  }
}

