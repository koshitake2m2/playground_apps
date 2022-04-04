#![allow(unused_imports)]
#![allow(dead_code)]
use crate::common::StdResult;
use crate::model::todo::*;
use std::borrow::Borrow;
use std::error::Error;
use std::fmt::{Display, Formatter};

use crate::TodoDao;
use crate::TodoDaoMysql;
use actix_web::http::StatusCode;
use actix_web::web::Json;
use actix_web::{
    delete, get, http::header::CONTENT_TYPE, post, put, web, App, HttpResponse, HttpServer,
    Responder, ResponseError,
};
use derive_more::{Display, Error};
use futures::StreamExt;
use mysql::error;
use serde::Deserialize;
use serde::Serialize;

struct TodoState {
    dao: Box<dyn TodoDao>,
}

#[get("")]
async fn get_all_todos(data: web::Data<TodoState>) -> actix_web::Result<impl Responder> {
    // TODO: add error handling.
    #[derive(Serialize, Debug)]
    struct ResponseBody {
        todos: Vec<Todo>,
    }
    let result = data
        .dao
        .select_all()
        .map_or(ResponseBody { todos: Vec::new() }, |todos| ResponseBody {
            todos,
        });
    println!("{:?}", result);
    Ok(web::Json(result))
}

#[get("{raw_todo_id}")]
async fn get_todo_by_id(
    data: web::Data<TodoState>,
    path: web::Path<(i32,)>,
) -> actix_web::Result<impl Responder> {
    // TODO: add error handling.

    let todo_id = TodoId::new(path.into_inner().0);
    let result = data
        .dao
        .select_by(todo_id)
        .map_or(None, |maybe_todo| maybe_todo);
    println!("{:?}", result);
    Ok(web::Json(result))
}

#[derive(Deserialize)]
struct CreateRequest {
    title: String,
    status: String,
}

#[post("")]
async fn create_todo(
    data: web::Data<TodoState>,
    request: web::Json<CreateRequest>,
) -> actix_web::Result<impl Responder> {
    // TODO: add validation.
    // TODO: add error handling.
    #[derive(Serialize, Debug)]
    struct ResponseBody {}
    let new_todo: NewTodo = NewTodo {
        title: TodoTitle::new(&request.title),
        status: TodoStatus::new(&request.status).unwrap(),
    };
    let result = data
        .dao
        .insert(new_todo)
        .map_or(ResponseBody {}, |_res| ResponseBody {});

    println!("{:?}", result);
    Ok(web::Json(result))
}

#[derive(Deserialize)]
struct UpdateRequest {
    title: String,
    status: String,
}

#[put("{raw_todo_id}")]
async fn update_todo_by_id(
    data: web::Data<TodoState>,
    path: web::Path<(i32,)>,
    request: web::Json<UpdateRequest>,
) -> actix_web::Result<impl Responder> {
    #[derive(Serialize, Debug)]
    struct ResponseBody {}
    let todo_id = TodoId::new(path.into_inner().0);
    // TODO: add validation.
    // TODO: add error handling.
    let update_todo: Todo = Todo {
        id: todo_id,
        title: TodoTitle::new(&request.title),
        status: TodoStatus::new(&request.status).unwrap(),
    };
    let result = data
        .dao
        .update(update_todo)
        .map_or(ResponseBody {}, |_res| ResponseBody {});
    println!("{:?}", result);
    Ok(web::Json(result))
}

#[delete("{raw_todo_id}")]
async fn delete_todo_by_id(
    data: web::Data<TodoState>,
    path: web::Path<(i32,)>,
) -> actix_web::Result<impl Responder> {
    #[derive(Serialize, Debug)]
    struct ResponseBody {}
    // TODO: add error handling.
    let todo_id = TodoId::new(path.into_inner().0);
    let result = data
        .dao
        .delete_by(todo_id)
        .map_or(ResponseBody {}, |_res| ResponseBody {});
    println!("{:?}", result);
    Ok(web::Json(result))
}

pub fn todo_config(cfg: &mut web::ServiceConfig) {
    let state = TodoState {
        dao: TodoDaoMysql::new(),
    };

    cfg.app_data(web::Data::new(state))
        .service(get_all_todos)
        .service(get_todo_by_id)
        .service(create_todo)
        .service(update_todo_by_id)
        .service(delete_todo_by_id);
}
