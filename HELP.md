# Spring webflux demo

The purpose of this demo is creating two Microservices, airports and flights that will expose reactive APIs

Then the fake api gateway will not just route this reactive endpoints but also add a new api composition pattern endpoint
in which the flights and airport information will be combined.

The databases will be MongoDbs database with the reactive driver.
In the future we may change one mongo to one postgress to also learn how to do it with a SQL driver

## Example

For example: in the flights subdomain model the departure and landing airport will store only the airport code
in airports microservice it will have detailed information about each airport. The point will be to combine with each
flight, information of the airport in a specific DTO.

All of this should be better in performance using reactives APIs because we will subscribe to the endpoint doing a fork
join at the end

TODO: Add a draw in the future
