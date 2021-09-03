module.exports = {
    devServer: {
      port: 8081,
      open: false, // 自动打开浏览器
      https: false,
      hot: true,
      inline: true, // 控制是否热更新
      proxy: {
        '/xhr': {
          target: 'http://127.0.0.1:8080/graphql',
          changeOrigin: true,
          secure: false,
          ws: true, // 代理转发 websockets
          pathRewrite: { // 路径重写
            '^/xhr': ''
          }
        }
      },
    }
  }