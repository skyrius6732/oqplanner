const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  //publicPath: 라우팅이 발생할 때 정적 파일의 경로를 지정합니다. 위 설정은 루트 경로(/)에 대한 라우팅을 기본값으로 사용
  publicPath: process.env.NODE_ENV === 'production' ? '/oqplanner' : '/oqplanner',
  transpileDependencies: true,
  outputDir: '../src/main/resources/static',

  // chainWebpack(config) { //빌드 시 빌드되어 나오는 js파일을 js폴더 아래로 묶어 빌드한다
  //   config.output.filename("js/[name].js"); 
  // },
  
  // indexPath: '../static/index.html',

  // outputDir: 'src/main/resources/static',
  // outputDir: 'dist',

  configureWebpack: {
    devtool: 'source-map'
  },

  // async rewrites() {
  //   return [
  //     {
  //       source: '/api/v1/:path*',
  //       destination: `${process.env.NEXT_PUBLIC_BACKEND}/api/v1/:path*`,
  //     },
  //   ];
  // },

  devServer: {
    proxy: 'http://localhost/oqplanner',
  },

  pluginOptions: {
    vuetify: {
			// https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
		}
  },


  // isCustomElement 설정 추가
  // compilerOptions: {
  //   isCustomElement: tag => tag.startsWith('v-')
  // }

  // compilerOptions: {
  //   isCustomElement: tag => tag === 'v-date-picker'
  // }
})
