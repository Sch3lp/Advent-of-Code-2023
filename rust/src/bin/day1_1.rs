use std::fmt::Display;
use std::io::Read;
use std::{i32, io};

fn main() {
    read_and_write(parse, calibration_sum);
}

fn calibration_sum(calibration_values: Vec<i32>) -> i32 {
    calibration_values.iter().sum()
}

fn parse(input: &str) -> Vec<i32> {
    input
        .lines()
        .map(|line| find_calibration_value(line))
        .map(|x| x.parse().unwrap())
        .collect()
}

fn find_calibration_value(line: &str) -> String {
    let first_number = find_first_number(line);
    let last_number = find_last_number(line);

    let calibration_value = String::from_iter([first_number, last_number].iter());
    calibration_value
}

fn find_first_number(line: &str) -> char {
    line.chars().find(|x| x.is_numeric()).unwrap_or_default()
}

fn find_last_number(line: &str) -> char {
    line.chars()
        .rev()
        .find(|x| x.is_numeric())
        .unwrap_or_default()
}

#[cfg(test)]
mod tests {
    use super::*;
    use indoc::indoc;

    const INPUT: &str = indoc! {"
    1abc2
    pqr3stu8vwx
    a1b2c3d4e5f
    treb7uchet
    "};


    #[test]
    fn test_parse() {
        let expected = [12, 38, 15, 77];
        assert_eq!(parse(INPUT), expected);
    }

    #[test]
    fn test_calibration_sum() {
        assert_eq!(calibration_sum(parse(INPUT)), 142);
    }
}

fn read_and_write<T, S: Display>(parse: fn(&str) -> T, compute: fn(T) -> S) {
    let mut input = String::new();

    io::stdin()
        .read_to_string(&mut input)
        .expect("Failed to read input");

    let result = compute(parse(&input));
    println!("{}", result);
}
