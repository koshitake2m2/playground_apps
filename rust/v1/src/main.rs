use actix_web::{web, App, HttpResponse, HttpServer, Responder};
use v1::todo_config;

async fn response404() -> impl Responder {
    HttpResponse::Ok().body("Hey 404!")
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    const HOST: &str = "localhost";
    const PORT: u16 = 8080;
    HttpServer::new(|| {
        App::new()
            .wrap(actix_web::middleware::NormalizePath::trim())
            .service(web::scope("/api/v1/todos").configure(todo_config))
            .route("/", web::get().to(response404))
    })
    .bind((HOST, PORT))?
    .run()
    .await
}
