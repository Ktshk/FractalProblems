select problem_date, timezone, problem_text, difficulty, scenario, generator_id, device_id, username, points, hint_used from alisa.problem_of_the_day
join alisa.problems on alisa.problem_of_the_day.problem_id=alisa.problems.problem_id
join alisa.scenarios on alisa.scenarios.scenario_id=alisa.problems.scenario_id
join alisa.reference on alisa.reference.reference_id = alisa.problem_of_the_day.reference_id
join alisa.users on alisa.users.user_id=alisa.reference.user_id
left join alisa.solutions on alisa.solutions.reference_id=alisa.reference.reference_id and alisa.solutions.problem_id=alisa.problem_of_the_day.problem_id