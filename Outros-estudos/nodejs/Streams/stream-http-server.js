import http from 'node:http'
import {Transform} from 'node:stream'


class negativeConversor extends Transform {
    _transform(chunk, encoding, callback) {
      const transformedNumber = Number(chunk.toString()) *-1
  
      callback(null, Buffer.from(String(transformedNumber)))
    }
  }

const server = http.createServer((req, res) => {

})

server.listen(4040)