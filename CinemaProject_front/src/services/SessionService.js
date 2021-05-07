import AccountService from "./AccountService";
import BaseService from "./BaseService"

class SessionService {
    static async getActual() {
        return fetch(BaseService._baseUrl+'/sessions/actual',
            {
                headers: {
                    "Authorization": AccountService.getToken()
                }
            })
            .then(BaseService.handleError);
    }

    static async getAll() {
        return fetch(BaseService._baseUrl+'/sessions',
            {
                headers: {
                    "Authorization": AccountService.getToken()
                }
            })
            .then(BaseService.handleError);
    }

    static async removeSession(id) {
        const requestOptions = {
            method: 'DELETE',
            headers: { 
                'Content-Type': 'application/json',
                "Authorization": AccountService.getToken()
            }
        };
        return fetch(BaseService._baseUrl+'/sessions/' + id, requestOptions)
            .then(BaseService.handleError);
    }

    static async updateSession(id, sessionData){
        const requestOptions = {
            method: 'PUT',
            headers: { 
                'Content-Type': 'application/json',
                "Authorization": AccountService.getToken()
            },
            body: JSON.stringify(sessionData)
        };
        return fetch(BaseService._baseUrl+'/sessions/' + id, requestOptions)
            .then(BaseService.handleError);
    }
                  
    static async getSession(id) {
        return fetch(BaseService._baseUrl + '/sessions/' + id,
        {
            headers: {
                Authorization: AccountService.getToken()
            }
        })
            .then(BaseService.handleError);
    }
                     
    static async createSession(session) {
        const requestOptions = {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                "Authorization": AccountService.getToken()
            },
            body: JSON.stringify(session)
        };
        return fetch(BaseService._baseUrl+'/sessions', requestOptions)
            .then(BaseService.handleError);
    }

    static async cancelSession(id){
        const requestOptions = {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                "Authorization": AccountService.getToken()
            }
        };
        return fetch(BaseService._baseUrl+'/sessions/' + id + '/cancel', requestOptions)
            .then(BaseService.handleError);
    }

    static deleteSession= async (id) =>{
        console.log("SessionService.deleteSession(id):");
        console.log("id: " + id);
        const requestOptions = {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        };
        return fetch(BaseService._baseUrl+'/sessions/' + id, requestOptions)
            .then(response => {
                if (!response.ok) {
                    BaseService.handleResponseError(response);
                }
                return response.json();
            })
            .catch(error => {
                BaseService.handleError(error);
            });
    };

    static getTicketsList= async (id) =>{
        console.log("SessionService.getTicketsList(id):");
        console.log("id: " + id);
        return fetch(BaseService._baseUrl+'/sessions/'+id+'/tickets')
            .then(response => {
                if (!response.ok) {
                    BaseService.handleResponseError(response);
                }
                return response.json();
            })
            .catch(error => {
                BaseService.handleError(error);
            });
    };
}
export default SessionService;