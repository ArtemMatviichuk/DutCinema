import BaseService from "./BaseService"
import AccountService from "./AccountService";

class GenreService {
    static async getAll() {
        console.log("GenreService.getAll():");
        return fetch(BaseService._baseUrl+'/genres',{
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
    static async getById(id) {
        return fetch(BaseService._baseUrl +"/genres/" + id, {
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
    static addGenre= async (genre) =>{
        console.log("GenreService.addGenre(genre):");
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' , Authorization: AccountService.getToken()},
            body: JSON.stringify(genre),
        };
        return fetch(BaseService._baseUrl+'/genres', requestOptions)
            .then(response => {
                if (!response.ok) {
                    console.log(response)
                    BaseService.handleResponseError(response);
                }
                return response;
            })
            .catch(error => {
                BaseService.handleError(error);
            });
    };


    static deleteGenre= async (id) =>{
        console.log("GenreService.deleteGenre(id):");
        console.log("id: " + id);
        const requestOptions = {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json', Authorization: AccountService.getToken()}
        };
        return fetch(BaseService._baseUrl+'/genres/' + id, requestOptions)
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
    static updateGenre= async (id, movie) =>{
        console.log("GenreService.updateGenre(id, genre):");
        console.log("id: " + id);
        const requestOptions = {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' ,
                Authorization: AccountService.getToken()},
            body: JSON.stringify(movie)
        };
        return fetch(BaseService._baseUrl+'/genres/' + id, requestOptions)
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
    static isNameFree = async (name)=>{
        return fetch(BaseService._baseUrl+'/genres/check-' + name, {
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
}
export default GenreService;