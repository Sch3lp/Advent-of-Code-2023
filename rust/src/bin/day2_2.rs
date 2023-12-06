use std::collections::HashMap;
use std::collections::hash_map::RandomState;
use std::fmt::Display;
use std::io::Read;
use std::io;

fn main() {
    read_and_write(parse, game_power_sum);
}
#[derive(Clone, PartialEq, Debug)]
struct Game {
    id: u32,
    draws: Vec<Draw>,
}

#[derive(Clone, PartialEq, Debug)]
struct Draw {
    blue: u32,
    red: u32,
    green: u32,
}

fn game_power_sum(games: Vec<Game>) -> u32 {
    games.iter()
        .map(|game| power(game))
        .sum()
}

fn power(game: &Game) -> u32 {
    //make Draw comparable and pick minimum
    game.draws.iter().map(|draw| power_draw(draw)).min().unwrap_or_default()
}

//this won't work
fn power_draw(draw: &Draw) -> u32 {
    let red = if draw.red != 0 { draw.red } else { 1 };
    let blue = if draw.blue != 0 { draw.blue } else { 1 };
    let green = if draw.green != 0 { draw.green } else { 1 };
    red * blue * green
}

fn parse(input: &str) -> Vec<Game> {
    input
        .lines()
        .map(|line| {
            let split: Vec<&str> = line.splitn(2, ": ").collect();
            let id: u32 = split
                .first()
                .and_then(|g| g.split_once(" ").and_then(|(_,r)|r.parse().ok()))
                .unwrap();
            let draws = split.last().unwrap().split("; ").map(parse_draw).collect();
            Game {
                id: id,
                draws: draws,
            }
        })
        .collect()
}

fn parse_draw(input: &str) -> Draw {
    let props: HashMap<&str, &str, RandomState> = input.split(", ").map(|a| {
        let mut iter = a.splitn(2," ");
        let amount = iter.next().unwrap();
        let color = iter.next().unwrap();
        (color,amount)
    }).collect();
    Draw {
        red: get_or_default(&props, "red"),
        green: get_or_default(&props, "green"),
        blue: get_or_default(&props, "blue"),
    }
}

fn get_or_default(props: &HashMap<&str, &str, RandomState>, key: &str) -> u32{
    props.get(key).map(|x| x.parse().unwrap_or_default()).unwrap_or_default()
}

#[cfg(test)]
mod tests {
    use super::*;
    use indoc::indoc;

    const INPUT: &str = indoc! {"
    Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
    Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
    Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
    Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    Game 21: 1 red, 1 blue, 1 green
    "};

    #[test]
    fn test_game_power_sum() {
        assert_eq!(game_power_sum(parse(INPUT)), 2286);
    }

    #[test]
    fn test_game_power() {
        let game1 = Game {
            id: 1,
            draws: vec![
                Draw { red: 4, green: 0, blue: 3 },
                Draw { red: 1, green: 2, blue: 6 },
                Draw { red: 0, green: 2, blue: 0 },
            ],
        };
        assert_eq!(power(&game1), 48)
    }

    #[test]
    fn test_parse() {
        let expected = [Game {
            id: 1,
            draws: vec![
                Draw { red: 4, green: 0, blue: 3 },
                Draw { red: 1, green: 2, blue: 6 },
                Draw { red: 0, green: 2, blue: 0 },
            ],
        },Game {
            id: 2,
            draws: vec![
                Draw { blue: 1,  green: 2, red: 0},
                Draw { green: 3,  blue: 4, red: 1},
                Draw { green: 1,  blue: 1, red: 0},
            ],
        },Game {
            id: 3,
            draws: vec![
                Draw {green: 8 , blue: 6, red: 20},
                Draw {blue: 5 , red: 4, green: 13},
                Draw {green: 5 , red: 1, blue: 0},
            ],
        },Game {
            id: 4,
            draws: vec![
                Draw{green: 1 , red: 3, blue: 6 },
                Draw{green: 3 , red: 6, blue: 0 },
                Draw{green: 3 , blue: 15, red: 14},
            ],
        },Game {
            id: 5,
            draws: vec![
                Draw { red: 6, blue: 1, green: 3},
                Draw { blue: 2, red: 1, green: 2},
            ],
        },Game {
            id: 21,
            draws: vec![
                Draw { red: 1, blue: 1, green: 1},
            ],
        },
        ];
        assert_eq!(parse(INPUT), expected);
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
