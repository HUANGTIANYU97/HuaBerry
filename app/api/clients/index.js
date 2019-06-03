const { Router } = require('express');
const { Client } = require('../../models');

const router = new Router();
router.post('/add/', (req, res) => {
  Client.find({}, { id: 1 }, (err, list) => {
    const num = list.length;
    const demo = {
      id: num + 1,
      name: 'Paul',
      number: num + 1,
    };
    Client.create(demo, (err2, resinfo) => {
      res.send(resinfo);
    });
  });
});
module.exports = router;
