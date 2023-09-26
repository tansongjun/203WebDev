import axios from 'axios';
import { useState } from 'react';


function PeopleService() {
    const PEOPLE_API_BASE_URL = "http://localhost:8080/api/v1/people";

    const getPeople = (auth) => {
        console.log(auth.user, auth.pwd);
        
        return axios.get(
            PEOPLE_API_BASE_URL,
            {
              auth: {
                username: `${auth.user}`,
                password: `${auth.pwd}`
              }
            })
    }

    const createPeople = (people, auth) => {
        return axios.post(PEOPLE_API_BASE_URL, people, {
            auth: {
                username: `${auth.user}`,
                password: `${auth.pwd}`
              }
        })
    }

    const getPeopleById = (peopleId, auth) => {
        return axios.get(PEOPLE_API_BASE_URL + '/' + peopleId, {
            auth: {
                username: `${auth.user}`,
                password: `${auth.pwd}`
              }
        })
    }

    const updatePeople = (people, peopleId, auth) =>{
        return axios.put(PEOPLE_API_BASE_URL + '/' + peopleId, people, {
            auth: {
                username: `${auth.user}`,
                password: `${auth.pwd}`
              }
        })
    }
    
    const deletePeople = (peopleId, auth) =>{
        return axios.delete(PEOPLE_API_BASE_URL + '/' + peopleId,   {
            auth: {
                username: `${auth.user}`,
                password: `${auth.pwd}`
              }
        })
    }
    return {
        getPeople: getPeople,
        createPeople: createPeople,
        getPeopleById: getPeopleById,
        updatePeople: updatePeople,
        deletePeople: deletePeople
    };
}

export default PeopleService();
