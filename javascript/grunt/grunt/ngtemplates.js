module.exports = {
  dev: {
    src: [
      '<%= htmlPath %>/**/*.html'
    ],
    dest: '<%= jsPath %>/templates_module.js',
    options: {
      module: 'ni.Templates',
      standalone: true,
      htmlmin: {
        collapseWhitespace: true,
        collapseBooleanAttributes: true
      },
      url: function(url) {
        var
          urlArr = url.split('/');

        return urlArr[urlArr.length-1];
      }
    }
  }
};