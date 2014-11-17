module.exports = {
  dist: {
    options: {
      compress: {
        drop_console: false
      }
    },
    files: {
      '<%= distPath %>/<%= appName %>.min.js': [
        '<%= distPath %>/<%= appName %>.js'
      ]
    }
  }
};