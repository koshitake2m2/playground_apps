use crate::common::StdResult;
use crate::model::todo::*;

pub trait TodoDao {
    fn select_all(&self) -> StdResult<Vec<Todo>>;
    fn select_by(&self, id: TodoId) -> StdResult<Option<Todo>>;
    fn insert(&self, new_todo: NewTodo) -> StdResult<()>;
    fn update(&self, todo: Todo) -> StdResult<()>;
    fn delete_by(&self, id: TodoId) -> StdResult<()>;
}
