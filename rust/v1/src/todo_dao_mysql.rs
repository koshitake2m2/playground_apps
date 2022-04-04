use crate::common::StdResult;
use crate::model::todo::*;
use crate::todo_dao::TodoDao;
use mysql::prelude::*;
use mysql::*;

// TODO: refactor this file.

pub struct TodoDaoMysql {}

impl TodoDaoMysql {
    pub fn new() -> Box<dyn TodoDao> {
        Box::new(TodoDaoMysql {}) as Box<dyn TodoDao>
    }

    // TODO: dependency injection
    fn conn() -> Result<PooledConn> {
        // TODO: move config file.
        let url = Opts::from_url("mysql://root:password@localhost:3310/sample_db")?;

        let pool: Pool = Pool::new(url)?;

        let conn: PooledConn = pool.get_conn()?;
        Ok(conn)
    }
}

fn convert_std<T>(from: Result<T>) -> StdResult<T> {
    match from {
        Ok(ok) => Ok(ok),
        Err(e) => {
            let new_error: Box<dyn std::error::Error> = Box::new(e) as Box<dyn std::error::Error>;
            Err(new_error)
        }
    }
}

impl TodoDao for TodoDaoMysql {
    fn select_all(&self) -> StdResult<Vec<Todo>> {
        let mut conn = convert_std(TodoDaoMysql::conn())?;
        let rows: Result<Vec<(i32, String, String)>> = conn.query(
            "select todo_id, todo_title, todo_status
         from sample_db.todo",
        );
        let selected_todos: Result<Vec<Todo>> = rows.map(|raw_todos| {
            raw_todos
                .iter()
                .map(|(todo_id, todo_title, todo_status)| {
                    Todo::new(
                        TodoId::new(*todo_id),
                        TodoTitle::new(todo_title),
                        TodoStatus::new(todo_status).unwrap(), // TODO: remove unwrap
                    )
                })
                .collect()
        });
        convert_std(selected_todos)
    }

    fn select_by(&self, id: TodoId) -> StdResult<Option<Todo>> {
        let mut conn = convert_std(TodoDaoMysql::conn())?;

        let maybe_row: Result<Option<(i32, String, String)>> = conn.exec_first(
            "select todo_id, todo_title, todo_status
         from sample_db.todo where todo_id = ?",
            (id.to_i32(),),
        );
        let maybe_selected_todo: Result<Option<Todo>> = maybe_row.map(|maybe_raw_todo| {
            maybe_raw_todo.map(|(todo_id, todo_title, todo_status)| {
                Todo::new(
                    TodoId::new(todo_id),
                    TodoTitle::new(&todo_title),
                    TodoStatus::new(&todo_status).unwrap(), // TODO: remove unwrap
                )
            })
        });

        convert_std(maybe_selected_todo)
    }

    fn insert(&self, new_todo: NewTodo) -> StdResult<()> {
        let mut conn = convert_std(TodoDaoMysql::conn())?;
        let result = conn.exec_drop(
            r"insert into sample_db.todo (todo_title, todo_status) values (?, ?)",
            (new_todo.title.to_string(), new_todo.status.to_string()),
        );
        println!("db result: {:?}", result);
        convert_std(result)
    }

    fn update(&self, todo: Todo) -> StdResult<()> {
        let mut conn = convert_std(TodoDaoMysql::conn())?;
        let result = conn.exec_drop(
            "update sample_db.todo set todo_title = ?, todo_status = ? where todo_id = ?",
            (
                todo.title.to_string(),
                todo.status.to_string(),
                todo.id.to_i32(),
            ),
        );
        println!("db result: {:?}", result);
        convert_std(result)
    }

    fn delete_by(&self, id: TodoId) -> StdResult<()> {
        let mut conn = convert_std(TodoDaoMysql::conn())?;
        let result = conn.exec_drop(
            "delete from sample_db.todo where todo_id = ?",
            (id.to_i32(),),
        );
        println!("db result: {:?}", result);
        convert_std(result)
    }
}
