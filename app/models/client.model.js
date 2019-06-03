const mongoose = require('mongoose');

const clientSchema = new mongoose.Schema({
  id: {
    type: Number,
    required: true,
  },
  name: {
    type: String,
    required: true,
  },
  number: {
    type: Number,
    required: true,
  },
}, { versionKey: false });
module.exports = mongoose.model('Client', clientSchema, 'client');
