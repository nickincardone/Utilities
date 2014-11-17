module.exports = {
  scripts: {
    files: ['<%= jsPath %>/**/*.js'],
    tasks: ['build:dist'],
    options: {

    }
  },
  styles: {
    files: [
      '<%= cssPath %>/**/*.scss',
      '!<%= cssPath %>/**/styles.css',
    ],
    tasks: ['build:dist']
  },
  templates:  {
    files: [
      '<%= htmlPath %>/**/*.html'
    ],
    tasks: ['build:dist']
  }
};