import axios from 'axios';

const PEOPLE_API_BASE_URL = "http://localhost:8080/api/v1/people";

class PeopleService {

    getPeople() {
        return axios.get(PEOPLE_API_BASE_URL);
    }

    createPeople(people) {
        return axios.post(PEOPLE_API_BASE_URL, people);
    }

    getPeopleById(peopleId) {
        return axios.get(PEOPLE_API_BASE_URL + '/' + peopleId);
    }

    updatePeople(people, peopleId){
        return axios.put(PEOPLE_API_BASE_URL + '/' + peopleId, people);
    }
    
    deletePeople(peopleId){
        return axios.delete(PEOPLE_API_BASE_URL + '/' + peopleId);
    }
}

export default new PeopleService();
