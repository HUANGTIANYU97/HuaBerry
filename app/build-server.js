const cors = require('cors');
const morgan = require('morgan');
const mongoose = require('mongoose');
const express = require('express');
const bodyParser = require('body-parser');
const api = require('./api');

const dbUrl = 'mongodb://127.0.0.1:27017/mydb';

module.exports = (cb) => {
  mongoose.connect(dbUrl, {
    useNewUrlParser: true,
  }).then(() => {
    console.log('mongoDB Connected');
  }).catch((err) => {
    console.log(err);
  });
  const app = express();
  app.disable('x-powered-by');
  app.use(cors());
  app.use(bodyParser.json({}));
  app.use(morgan('[:date[iso]] :method :url :status :response-time ms - :res[content-length]'));
  app.use('/api', api);
  app.use('*', (req, res) => res.status(404).end());
  const server = app.listen(process.env.PORT || 9400, () => cb && cb(server));
};
