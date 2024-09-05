/**
 * Pipe() -> 'canaliza' os dados para uma saída, gerencia para onde o fluxo de dados deve ir
 * Push() -> Permite a Readable Stream fornecer dados para quem estiver a consumindo
 * Streams não interpretam tipos primitivos, apenas Buffer
 * Buffers recebem Strings como parâmetro
 *
 * Quando eu envio um fluxo de dados por meio de uma Readable Stream, a recepção desses dados na Writable Stream acontece nos chunks
 */

import { Readable, Writable, Transform } from "node:stream";

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

class negativeConversor extends Transform {
  _transform(chunk, encoding, callback) {
    const transformedNumber = Number(chunk.toString()) *-1

    callback(null, Buffer.from(String(transformedNumber)))
  }
}

class multiplyByTen extends Writable {
  _write(chunk, encoding, callback) {
    console.log(chunk * 10 + "\tManipulado no nível 2.");

    callback();
  }
}

new countUntilAHundred() // Precisa LER dados de uma fonte de dados
    .pipe(new negativeConversor) // Precisa LER dados de algum lugar e ESCREVER dados para algum lugar
    .pipe(new multiplyByTen()); // Precisa ESCREVER dados para algum lugar
