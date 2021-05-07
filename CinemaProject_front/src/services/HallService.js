import AccountService from "./AccountService";
import BaseService from "./BaseService"

class HallService{
    static async getAll() {
        return fetch('http://localhost:8081/halls',
            {
                headers: {
                    Authorization: AccountService.getToken()
                }
            })
            .then(BaseService.handleError);
    }
    static isNameFree = async (name)=>{
        return fetch(BaseService._baseUrl+'/halls/check-' + name, {
            headers: {
                Authorization: AccountService.getToken()
            }})
            .then(response => {
                if (!response.ok) {
                    BaseService.handleResponseError(response);
                }
                return response.json();
            })
            .catch(error => {
                BaseService.handleError(error);
            });
    }


    static getById= async (id) =>{
        console.log("HallService.getById(id):");
        console.log("id: " + id);
        return fetch(BaseService._baseUrl+'/halls/'+id,
            {
                headers: {
                    Authorization: AccountService.getToken()
                }})
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
    static addHall= async (hall) =>{
        console.log("HallService.addHall(hall):");
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' , Authorization: AccountService.getToken()},
            body: JSON.stringify(hall),
        };
        return fetch(BaseService._baseUrl+'/halls', requestOptions)
            .then(response => {
                if (!response.ok) {
                    BaseService.handleResponseError(response);
                }
                return response;
            })
            .catch(error => {
                BaseService.handleError(error);
            });
    };
    static deleteHall= async (id) =>{
        console.log("HallService.deleteHall(id):");
        console.log("id: " + id);
        const requestOptions = {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json',
                Authorization: AccountService.getToken()}
        };
        return fetch(BaseService._baseUrl+'/halls/' + id, requestOptions)
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
    static updateHall= async (id, hall) =>{
        console.log("HallService.updateHall(id, hall):");
        console.log("id: " + id);
        const requestOptions = {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json',
                Authorization: AccountService.getToken()
             },
            body: JSON.stringify(hall)
        };
        return fetch(BaseService._baseUrl+'/halls/' + id, requestOptions)
            .then(response => {
                if (!response.ok) {
                    BaseService.handleResponseError(response);
                    return response;
                }
                return response;
            })
            .catch(error => {
                BaseService.handleError(error);
            });
    };

    static blockHall= async (id) =>{
        console.log("HallService.blockHall(id):");
        console.log("id: " + id);
        const requestOptions = {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' ,
                Authorization: AccountService.getToken()}
        };
        return fetch(BaseService._baseUrl+'/halls/block/' + id, requestOptions)
            .then(response => {
                if (!response.ok) {
                    BaseService.handleResponseError(response);
                }
                return response;
            })
            .catch(error => {
                BaseService.handleError(error);
            });
    };
    static unblockHall= async (id) =>{
        console.log("HallService.unblockHall(id):");
        console.log("id: " + id);
        const requestOptions = {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' ,
                Authorization: AccountService.getToken()}
        };
        return fetch(BaseService._baseUrl+'/halls/unblock/' + id, requestOptions)
            .then(response => {
                if (!response.ok) {
                    BaseService.handleResponseError(response);
                }
                return response;
            })
            .catch(error => {
                BaseService.handleError(error);
            });
    };
    static async getAllActive() {
        console.log("HallService.getActive():");
        return fetch(BaseService._baseUrl+'/halls/active',{
            headers: {
                Authorization: AccountService.getToken()
            }})
            .then(response => {
                if (!response.ok) {
                    BaseService.handleResponseError(response);
                }
                return response.json();
            })
            .catch(error => {
                BaseService.handleError(error);
            });
    }
    static async getBlocked() {
        console.log("HallService.getBlocked():");
        return fetch(BaseService._baseUrl+'/halls/blocked',{
            headers: {
                Authorization: AccountService.getToken()
            }})
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
export default HallService;