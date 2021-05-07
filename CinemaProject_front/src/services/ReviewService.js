import AccountService from "./AccountService";
import BaseService from "./BaseService"

class ReviewService{
    static async addReview(movieId, text) {
        const requestOptions = {
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json',
                Authorization: AccountService.getToken()
            },
            body: JSON.stringify({
                movieId: (Number)(movieId),
                text: text,
                authorId: AccountService.getId()
            })
        }
        return fetch(BaseService._baseUrl+'/reviews', requestOptions)
            .then(BaseService.handleError);
    }

    static async getListOfMovieReviews(id) {
        return fetch(BaseService._baseUrl + '/reviews/movie/' + id,
            {
                headers: {
                    "Authorization": AccountService.getToken()
                }
            })
            .then(BaseService.handleError);
    }

    static async updateReview(id, newText){
        const requestOptions = {
            method: 'PUT', 
            headers: {
                'Content-Type': 'application/json',
                Authorization: AccountService.getToken()
            },
            body: newText
        }
        return fetch(BaseService._baseUrl+'/reviews/' + id, requestOptions)
            .then(BaseService.handleError);
    }

    static async deleteReview(id){
        const requestOptions = {
            method: 'DELETE', 
            headers: {
                'Content-Type': 'application/json',
                Authorization: AccountService.getToken()
            }
        }
        return fetch(BaseService._baseUrl+'/reviews/' + id, requestOptions)
            .then(BaseService.handleError);
    }

    static async getAll() {
        return fetch(BaseService._baseUrl + '/reviews', {
				headers: {
					Authorization: AccountService.getToken(),
				},
			}).then(BaseService.handleError)
    }

    static getById= async (id) =>{
        console.log("ReviewService.getById(id):");
        console.log("id: " + id);
        return fetch(BaseService._baseUrl+'/reviews/'+id)
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
export default ReviewService;