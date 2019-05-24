select alisa.reference.device_id, username, problem_text, difficulty, scenario, points, hint_used 
from alisa.reference, alisa.devices, alisa.users, alisa.problems, alisa.solutions, alisa.scenarios 
where alisa.reference.device_id = alisa.devices.device_id and alisa.users.user_id = alisa.reference.user_id
and alisa.solutions.reference_id = alisa.reference.reference_id and alisa.solutions.problem_id = alisa.problems.problem_id
and alisa.problems.scenario_id = alisa.scenarios.scenario_id