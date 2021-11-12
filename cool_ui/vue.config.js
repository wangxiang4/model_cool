//使用严格模式
'use strict'
const path = require('path')
const defaultSettings = require('./src/settings.js')
//生成绝对路径函数
function resolve(dir) {
  return path.join(__dirname, dir)
}
const name = defaultSettings.title || '模酷酷' // 标题
const port = process.env.port || 80 // 端口

//vue.config.js 配置说明
//官方vue.config.js 参考文档 https://cli.vuejs.org/zh/config/#css-loaderoptions
//这里只列一部分，具体配置参考文档
module.exports={
  //部署生产环境和开发环境下的URL。
  //默认情况下，Vue CLI 会假设你的应用是被部署在一个域名的根路径上
  publicPath: process.env.NODE_ENV === "production" ? "/" : "/",
  //在npm run build 或 yarn build 时 ，生成文件的目录名称（要和baseUrl的生产环境路径一致）（默认dist）
  outputDir: 'dist',
  //用于放置生成的静态资源 (js、css、img、fonts) 的；（项目打包之后，静态资源会放在这个文件夹下）
  assetsDir: 'static',
  //是否开启eslint保存检测，有效值：ture | false | 'error'
  lintOnSave: process.env.NODE_ENV === 'development',
  //关闭生产环境的source mapENV,否则生产环境里可以看到所有的源码。
  productionSourceMap: false,
  //webpack-dev-server 相关配置
  devServer: {
    port: port,
    //代理后台服务器地址
    proxy: {
      //detail:https://cli.vuejs.org/config/#devserver-proxy
      [process.env.VUE_APP_BASE_API+"/"]: {
        target: `http://localhost:9300`,
        //设置跨域请求
        changeOrigin: true,
        //更改代理服务器代理真实根路径地址
        pathRewrite: {
          ['^'+process.env.VUE_APP_BASE_API]:''
        }
      }
    },
    //开启可以使用hostname
    disableHostCheck: true
  },
  configureWebpack:{
    name: name,
    resolve: {
      alias: {
        '@': resolve('src')
      }
    }
  },
  //WebPack链式配置(配置一些转换器一写底层支撑的一些配置)
  chainWebpack(config) {


    // 修改svg规则file-loader排除svg图标目录
    config.module
      .rule('svg')
      .exclude.add(resolve('src/assets/icons'))
      .end()
    // set svg-sprite-loader
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/assets/icons'))
      .end() // 匹配只包含src/assets/icons目录种的svg文件
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]' //设置使用symbol唯一ID
      })
      .end()


    // set preserveWhitespace
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => {
        options.compilerOptions.preserveWhitespace = true
        return options
      }).end()


    config
      //https://juejin.im/post/6844903450644316174
      .when(process.env.NODE_ENV === 'development',
        config => config.devtool('cheap-source-map')
      )


    //参考:https://juejin.im/entry/6844903661441646605
    config
      .when(process.env.NODE_ENV !== 'development',
        config => {
          //将所有runtime.js合并到html中
          config.plugin('ScriptExtHtmlWebpackPlugin')
            .after('html')
            .use('script-ext-html-webpack-plugin', [{
              // `runtime` must same as runtimeChunk name. default is `runtime`
              inline: /runtime\..*\.js$/
            }])
            .end()
          config
            .optimization.splitChunks({
            chunks: 'all',
            cacheGroups: {
              libs: {
                name: 'chunk-libs',
                test: /[\\/]node_modules[\\/]/,
                priority: 10,
                chunks: 'initial' // 只打包初始时依赖的第三方
              },
              elementUI: {
                name: 'chunk-elementUI', // 单独将 elementUI 拆包
                priority: 20, // 权重要大于 libs 和 app 不然会被打包进 libs 或者 app
                test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // in order to adapt to cnpm
              },
              commons: {
                name: 'chunk-commons',
                test: resolve('src/components'), // 可自定义拓展你的规则
                minChunks: 3, // 最小共用次数,超过打公共包去
                priority: 5,
                reuseExistingChunk: true //遇到相同模块直接用,不用创建公共模块
              }
            }
          })
          //设置runtimeChunk是将包含chunks 映射关系的 list单独从 app.js里提取出来，
          // 因为每一个 chunk 的 id 基本都是基于内容 hash 出来的，所以每次改动都会影响它，
          // 如果不将它提取出来的话，等于app.js每次都会改变。缓存就失效了
          config.optimization.runtimeChunk('single')
        }
      )

  }
}
