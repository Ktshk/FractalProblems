select * from alisa.reference, alisa.users, alisa.devices 
where alisa.reference.user_id=alisa.users.user_id and alisa.devices.device_id=alisa.reference.device_id
