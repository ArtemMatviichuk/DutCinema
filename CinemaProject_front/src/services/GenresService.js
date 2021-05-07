import AccountService from './AccountService'
import BaseService from './BaseService'

class GenresService {
    static async getAll(){
        return fetch('http://localhost:8081/genres/', {
				headers: {
					Authorization: AccountService.getToken(),
				},
			}).then(BaseService.handleError)
    }
}

export default GenresService
