select min(release.users.username), min(release.devices.device_num), sum(release.solutions.points) 
from release.users, release.solutions, release.reference, release.devices 
where release.reference.user_id=release.users.user_id and release.solutions.reference_id=release.reference.reference_id 
	and release.reference.device_id=release.devices.device_id
group by release.reference.reference_id
order by sum(release.solutions.points) desc