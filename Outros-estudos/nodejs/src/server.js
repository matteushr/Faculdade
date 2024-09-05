import http from "node:http";

//HTTP
// - Método HTTP
// - URL

//GET, POST, PUT, PATCH, DELETE

/**
 *  GET => Busca por um recurdo do back-end
 *  POST => Cria um resurso no back-end
 *  PUT => Atualizar um recurso no back-end
 *  PATCH => Atualizar uma informação específica de um recurso no back-end
 *
 *  Iremos divergir as rotas unicamente pela soma do método pela URL
 *
 *  /users:
 *
 *  - GET /users -> Buscando usuários no back-end
 *  - POST /users -> Criando usuário no back-end
 *
 *
 * Como o back-end vai informar pro front-end o tipo de dado? Através do cabeçalho
 * 
 * As informações do cabeçalho são metadados, informações adicionais que mostram como interpretar um dado
 * 
 * As resposta do tipo response acontecem após uma requisição do tipo require, geralmente do back-end pro front-end
 *
 */

const users = []
const adm = false

const server = http.createServer((req, res) => {
  const { method, url } = req;

  console.log(method, url);

  if (method === "GET" && url === "/users") {
    
    if(adm) {
        return res
        .setHeader('Content-type', 'application/json')
        .writeHead(200)
        .end(JSON.stringify(users));
    }

    return res.writeHead(403).end("Pagamanto não identificado.")
    
    
  }

  if(method === "POST" && url === '/users') {
    users.push({
        id: 1,
        name: "Marcinho do gás",
        email: "marcinhodogas@gmail.com"

    })
    return res
        .writeHead(201)
        .end('Usuário criado com sucesso')
}

  return res.writeHead(404).end("Hello fellow user! Unfortunately, we coudn't reach what you are looking for :-(");
});

server.listen(65444);
