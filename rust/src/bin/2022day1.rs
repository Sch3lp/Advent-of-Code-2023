use std::i32;

fn main() {
    println!("Hello, world!");
}

pub fn most_calories(list: Vec<Vec<i32>>) -> i32 {
    return list.iter()
        .map(|elf| elf.iter().sum())
        .max()
        .unwrap_or_default();
}

pub fn parse(input: &str) -> Vec<Vec<i32>> {
    let mut elf : Vec<i32> = Vec::new();
    let mut elves: Vec<Vec<i32>> = Vec::new();
    for line in input.lines() {
        if !line.is_empty() {
            elf.push(line.parse().unwrap());
        } else {
            elves.push(elf);
            elf = Vec::new();
        }
    }
    elves.push(elf);
    return elves;
}

#[cfg(test)]
mod tests {
    use super::*;
    use indoc::indoc;

    const INPUT: &str = indoc! {"
    1000
    2000
    3000
    
    4000
    
    5000
    6000
    
    7000
    8000
    9000
    
    10000
    "};

    #[test]
    fn test_parse() {
        let mut expected: Vec<Vec<i32>> = Vec::new();
        expected.push([1000,2000,3000].to_vec());
        expected.push([4000].to_vec());
        expected.push([5000,6000].to_vec());
        expected.push([7000,8000,9000].to_vec());
        expected.push([10000].to_vec());
        assert_eq!(parse(INPUT), expected);
    }

    #[test]
    fn test_most_calories() {
        assert_eq!(most_calories(parse(INPUT)), 24000);
    }
}