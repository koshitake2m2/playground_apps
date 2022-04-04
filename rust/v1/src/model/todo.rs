#![allow(dead_code)]
use serde::ser::SerializeStruct;
use serde::{Serialize, Serializer};

#[derive(Clone, Copy, PartialEq, Eq, Debug)]
pub struct TodoId {
    value: i32,
}
impl TodoId {
    pub fn new(value: i32) -> Self {
        Self { value }
    }
    pub fn to_i32(&self) -> i32 {
        self.value
    }
}

#[derive(Clone, PartialEq, Eq, Debug)]
pub struct TodoTitle {
    value: String,
}
impl TodoTitle {
    pub fn new(value: &str) -> Self {
        Self {
            value: value.to_string(),
        }
    }
    pub fn to_string(&self) -> String {
        self.value.clone()
    }
}

#[derive(Clone, Copy, PartialEq, Eq, Debug)]
pub enum TodoStatus {
    Waiting,
    Doing,
    Done,
}

impl TodoStatus {
    pub fn new(value: &str) -> Result<Self, TodoStatusError> {
        match value {
            "waiting" => Ok(TodoStatus::Waiting),
            "doing" => Ok(TodoStatus::Doing),
            "done" => Ok(TodoStatus::Done),
            _ => Err(TodoStatusError {
                message: format!("input: {} is not match for todo status", value).to_string(),
            }),
        }
    }

    pub fn to_string(&self) -> String {
        match self {
            TodoStatus::Waiting => "waiting",
            TodoStatus::Doing => "doing",
            TodoStatus::Done => "done",
        }
        .to_string()
    }
}

#[derive(Clone, PartialEq, Eq, Debug)]
pub struct TodoStatusError {
    message: String,
}

#[derive(Clone, PartialEq, Eq, Debug)]
pub struct NewTodo {
    pub title: TodoTitle,
    pub status: TodoStatus,
}

impl NewTodo {
    pub fn new(title: TodoTitle, status: TodoStatus) -> Self {
        Self {
            title: title.clone(),
            status: status.clone(),
        }
    }
}

#[derive(Clone, PartialEq, Eq, Debug)]
pub struct Todo {
    pub id: TodoId,
    pub title: TodoTitle,
    pub status: TodoStatus,
}

impl Todo {
    pub fn new(id: TodoId, title: TodoTitle, status: TodoStatus) -> Self {
        Self {
            id: id.clone(),
            title: title.clone(),
            status: status.clone(),
        }
    }
}

impl Serialize for Todo {
    fn serialize<S>(&self, serializer: S) -> Result<S::Ok, S::Error>
    where
        S: Serializer,
    {
        let mut s = serializer.serialize_struct("Todo", 3)?;
        s.serialize_field("id", &self.id.to_i32())?;
        s.serialize_field("title", &self.title.to_string())?;
        s.serialize_field("status", &self.status.to_string())?;
        s.end()
    }
}
