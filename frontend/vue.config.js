const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: '../src/main/resources/static',

  devServer: {
    proxy: 'http://localhost'
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
