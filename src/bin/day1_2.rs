use std::collections::hash_map::RandomState;
use std::collections::HashMap;
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
    let digits: HashMap<&str, i32, RandomState> = HashMap::from_iter([
        ("one", 1),
        ("two", 2),
        ("three", 3),
        ("four", 4),
        ("five", 5),
        ("six", 6),
        ("seven", 7),
        ("eight", 8),
        ("nine", 9),
        ("zero", 0),
        ("1", 1),
        ("2", 2),
        ("3", 3),
        ("4", 4),
        ("5", 5),
        ("6", 6),
        ("7", 7),
        ("8", 8),
        ("9", 9),
        ("0", 0),
    ]);

    let mut matched_indexes: Vec<(usize, i32)> = digits
        .keys()
        .filter_map(|k| line.find(k).map(|idx| (idx, digits[k])))
        .collect();
    matched_indexes.sort_by(|a, b| a.0.partial_cmp(&b.0).unwrap());
    let first_number = matched_indexes.first().unwrap().1;
    let last_number = matched_indexes.last().unwrap().1;
    format!("{}{}", first_number, last_number)
}

#[cfg(test)]
mod tests {
    use super::*;
    use indoc::indoc;

    const INPUT: &str = indoc! {"
    two1nine
    eightwothree
    abcone2threexyz
    xtwone3four
    4nineeightseven2
    zoneight234
    7pqrstsixteen
    "};

    #[test]
    fn test_parse() {
        let expected = [29, 83, 13, 24, 42, 14, 76];
        assert_eq!(parse(INPUT), expected);
    }

    #[test]
    fn test_calibration_sum() {
        assert_eq!(calibration_sum(parse(INPUT)), 281);
    }

    #[test]
    fn test_solve2() {
        assert!(calibration_sum(parse(INPUT)) > 54196, "solution must be larger than 54196");
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
