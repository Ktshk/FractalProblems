select min(release.users.username), release.devices.device_id, sum(release.solutions.points)
from release.users, release.reference, release.devices, release.solutions
where release.reference.user_id=release.users.user_id and release.solutions.reference_id=release.reference.reference_id
and release.reference.device_id=release.devices.device_id 
group by release.devices.device_id
order by sum(release.solutions.points) desc
