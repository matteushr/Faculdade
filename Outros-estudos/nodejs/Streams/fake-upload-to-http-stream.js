import {Readable} from 'node:stream'

class countUntilAHundred extends Readable {
    index = 1;
  
    _read() {
      const i = this.index++;
  
      setTimeout(() => {
        if (i <= 100) {
          const buf = Buffer.from(String(i));
          this.push(buf);
        } else {
          this.push(null);
        }
      }, 100);
    }
  }

  fetch('http://localhost:4040', {
    method: 'POST',
    body: new countUntilAHundred(),
  })