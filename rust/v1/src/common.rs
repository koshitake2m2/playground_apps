use std::error;
use std::result;

pub type StdResult<T> = result::Result<T, Box<dyn error::Error>>;
