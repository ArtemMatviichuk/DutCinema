import AccountService from "./AccountService";
import BaseService from "./BaseService"

class TicketService extends BaseService{
    static async getBySession(id) {
        return fetch(BaseService._baseUrl + '/sessions/' + id + '/tickets',
        {
            headers: {
                Authorization: AccountService.getToken()
            }
        })
            .then(BaseService.handleError)
    }

    static async getUsersTickets() {
        return fetch(BaseService._baseUrl + '/tickets/user/' + AccountService.getId(),
        {
            headers: {
                Authorization: AccountService.getToken()
            }
        })
            .then(BaseService.handleError)
    }

    static async purchaseTickets(id, ticketsData) {
        const requestOptions = {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                Authorization: AccountService.getToken()
            },
            body: JSON.stringify({
                sessionId: id,
                userId: AccountService.getId(),
                places: ticketsData
            })
        };
        
        return fetch(BaseService._baseUrl+'/tickets/purchaselist', requestOptions)
            .then(BaseService.handleError);
    }

    static async cancelBooking(id) {
        const requestOptions = {
            method: 'DELETE',
            headers: { 
                'Content-Type': 'application/json',
                Authorization: AccountService.getToken()
            }
        };
        
        return fetch(BaseService._baseUrl+'/tickets/' + id, requestOptions)
            .then(BaseService.handleError);
    }

    static purchaseTicket= async (purchaseTicket) =>{
        console.log("TicketService.purchaseTicket(purchase):");
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(purchaseTicket)
        };
        return fetch(BaseService._baseUrl+'/tickets/purchase', requestOptions)
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
export default TicketService;