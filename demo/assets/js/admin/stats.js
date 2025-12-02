var AdminStats = {
  template:
    '<div>' +
    '<div style=display:flex;justify-content:flex-end;' +
    'margin-bottom:12px;gap:12px>' +
    '<el-select placeholder=时间范围 style=width:160px>' +
    '<el-option label=近7天 value=7></el-option>' +
    '<el-option label=近30天 value=30></el-option>' +
    '</el-select>' +
    '<el-button>导出CSV</el-button>' +
    '</div>' +
    '<el-row :gutter=24>' +
    '<el-col :span=12><el-card>' +
    '<div id=s1 class=chart></div>' +
    '</el-card></el-col>' +
    '<el-col :span=12><el-card>' +
    '<div id=s2 class=chart></div>' +
    '</el-card></el-col>' +
    '</el-row>' +
    '<el-row :gutter=24 style=margin-top:24px>' +
    '<el-col :span=12><el-card>' +
    '<div id=s3 class=chart></div>' +
    '</el-card></el-col>' +
    '<el-col :span=12><el-card>' +
    '<div id=s4 class=chart></div>' +
    '</el-card></el-col>' +
    '</el-row>' +
    '</div>',
  mounted: function () {
    var s1 = echarts.init(document.getElementById('s1'))
    s1.setOption({
      xAxis: { type: 'category', data: ['1','2','3','4','5','6','7'] },
      yAxis: { type: 'value' },
      series: [{ type: 'line', data: [10,12,9,14,18,11,16] }]
    })
    var s2 = echarts.init(document.getElementById('s2'))
    s2.setOption({
      xAxis: { type: 'category', data: ['A','B','C','D'] },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: [1200,800,600,500] }]
    })
    var s3 = echarts.init(document.getElementById('s3'))
    s3.setOption({
      xAxis: { type: 'category', data: ['1','2','3','4','5','6','7'] },
      yAxis: { type: 'value' },
      series: [{ type: 'line', data: [100,120,90,140,180,110,160] }]
    })
    var s4 = echarts.init(document.getElementById('s4'))
    s4.setOption({
      xAxis: { type: 'category', data: ['Jan','Feb','Mar','Apr'] },
      yAxis: { type: 'value' },
      series: [{ type: 'line', data: [50,80,120,160] }]
    })
  }
}

