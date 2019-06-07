const mqtt = require('mqtt');
const mongoose = require('mongoose');

const { Client } = require('../../models');

const client = mqtt.connect('mqtt://172.20.10.8');

const dbUrl = 'mongodb://127.0.0.1:27017/mydb';

mongoose.connect(dbUrl, {
  useNewUrlParser: true,
}).then(() => {
  // console.log('mongoDB Connected');
}).catch((err) => {
  console.log(err);
});

client.on('connect', () => {
  // 订阅presence主题
  client.subscribe('button');
  // 向presence主题发布消息
});

client.on('message', (topic, message) => {
  // 收到的消息是一个Buffer
  console.log(message.toString());
  if (message.toString() === 'buttonDelete') {
    Client.find({}, {}, (err, list) => {
      const num = list.length;
      Client.deleteOne({ id: num }, (error, response) => {
        console.log(response);
      });
      Client.find({}, { _id: 0 }, (error2, responce) => {
        console.log(error2);
        console.log(responce);
        client.publish('len2', 'delete');
      });
    });
  } else if (message.toString() === 'buttonGet') {
    // mongoose.connect(dbUrl, {
    //   useNewUrlParser: true,
    // }).then(() => {
    //   // console.log('mongoDB Connected');
    // }).catch((err) => {
    //   console.log(err);
    // });
    Client.find({}, { _id: 0 }, (err, responce) => {
      console.log(responce.length);
      client.publish('len1', String(responce.length));
    });
  } else if (message.toString() === 'buttonAdd') {
    // mongoose.connect(dbUrl, {
    //   useNewUrlParser: true,
    // }).then(() => {
    //   // console.log('mongoDB Connected');
    // }).catch((err) => {
    //   console.log(err);
    // });
    Client.find({}, { id: 1 }, (err, list) => {
      const num = list.length;
      const demo = {
        id: num + 1,
        name: 'Paul',
        number: num + 1,
      };
      Client.create(demo, (error2, resinfo) => {
        console.log(error2);
        console.log(resinfo);
      });
      Client.find({}, { _id: 0 }, (error1, responce) => {
        console.log(responce.length + 1);
        client.publish('len2', String(responce.length + 1));
      });
    });
  } else {
    console.log('I dont know what does that mean!');
  }
});
