module.exports = {
  dev: {
    options: {
      style: 'expanded'
    },
    files: {
      'dist/assets/stylesheets/styles.css': '<%= cssPath %>/styles.scss'
    }
  },
  dist: {
    options: {
      style: 'compressed'
    },
    files: {
      'dist/assets/stylesheets/styles.min.css': 'dist/stylesheets/styles.css'
    }
  }
};