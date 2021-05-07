import React from 'react'
import './style.css'
import ErrorComponent from '../error/ErrorComponent';
import Loading from '../Loading/Loading';
import ReviewService from '../../services/ReviewService';
import AccountService from '../../services/AccountService';
import Review from './Review';

class Reviews extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            errorPost: null,
            isLoaded: false,
            id: props.id,
            reviews: [],
            newReviewText: ""
        };
        this.handleTextChange = this.handleTextChange.bind(this);
        this.postReview = this.postReview.bind(this);
    }

    async errorHandler(response){
        if (!response.ok){
            await response.json()
                    .then(res => {
                        throw Error(res.message)
                    })
        }

        return response.json()
    }
  
    componentDidMount() {
        ReviewService.getListOfMovieReviews(this.state.id)
            .then((result) => {
                    this.setState({
                        isLoaded: true,
                        reviews: result
                    }
                );
            })
            .catch(err => {
                this.setState({
                    isLoaded: true,
                    error: err
                })
            }
        );
    }

    onItemDelete(id){
        let index = this.state.reviews.findIndex(el => el.id == id)
        
        if (index > -1) {
            this.state.reviews.splice(index, 1);
            this.setState({})
        }
    }
  
    render() {
        const { error, isLoaded, reviews } = this.state;
        let newReviwForm = (
            <div className="text-left newReview col-md-12">
                <h5>Already watched? Share your impressions with others!</h5>
                <form onSubmit={this.postReview}>
                    <textarea className="col-md-12 newReview__text form-control" 
                        placeholder="Write your review (minimum 5 characters)" 
                        rows="5"
                        onChange={this.handleTextChange}
                        value={this.state.newReviewText}/>
                    {
                        this.state.errorPost &&
                        <p className="text-right error_reviewer_massage__item">{this.state.errorPost.message}</p>
                    }
                    <input type="submit" value="Post Review" className="newReview__submit buttonPost"/>
                </form>
            </div>
        )

        if (error) {
            return <ErrorComponent error={error} />;
        } else if (!isLoaded) {
            return <Loading />;
        } else if (reviews.length === 0){
            return <div>
                        <h3 className="text-left">No reviews for now</h3>
                        {newReviwForm}
                    </div>
        } else {
            return (
                <div>
                    <h3 className="text-left">Reviews:</h3>
                    {
                        reviews.map(el =>{
                            return <Review  key={el.id} review={el} onDelete={(id) => this.onItemDelete(id)} />
                        })
                    }
                    {newReviwForm}
                </div>
            );
        }
    }

    handleTextChange(event){
        this.setState({
            newReviewText: event.target.value
        });
    }

    postReview(event){
        event.preventDefault();

        if (this.state.newReviewText.length < 5){
            this.setState({
                errorPost: {
                    message: "Text is too short"
                }
            })
            return
        }

        ReviewService.addReview(this.state.id, this.state.newReviewText)
            .then(result => {
                    this.state.reviews.push({
                        id: result.id,
                        text: result.text,
                        creationDate: result.creationDate,
                        movieId: result.movieId,
                        authorId: result.authorId,
                        firstName: result.firstName,        
                        lastName: result.lastName            
                    })
                    this.setState({
                        newReviewText: ""
                    })
                    event.target.value = ""
            })
            .catch(err => {
                this.setState({
                    isLoaded: true,
                    errorPost: err
                })
            })
    }
}

export default Reviews;