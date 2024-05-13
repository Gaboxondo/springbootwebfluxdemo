print('Start creating databases ##########################')
db = db.getSiblingDB('flightsdb');
db.createUser(
    {
        user: 'flightsdbuser',
        pwd:  'sa',
        roles: [{role: 'readWrite', db: 'flightsdb'}],
    }
);
db.createCollection('flights');

db = db.getSiblingDB('airportsdb');
db.createUser(
    {
        user: 'airportsdbuser',
        pwd:  'api1234',
        roles: [{role: 'readWrite', db: 'airportsdb'}],
    }
);

db.createCollection('airports');
print('End creating databases ##########################')
