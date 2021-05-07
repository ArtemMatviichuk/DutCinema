import React from 'react'
import MovieService from '../../services/MovieService'
import GenresService from '../../services/GenresService'
import ErrorComponent from '../error/ErrorComponent'
import PageResult from './PageResult'
import './MovieListPage.css'
import Loading from '../Loading/Loading'
import { TiDelete } from 'react-icons/ti'

class MoviesListPage extends React.Component {
	constructor(props) {
		super(props)
		this.state = {
			error: null,
			isLoaded: false,
			movies: [],
			page: 1,
			perPage: 20,
			pageAmount: 0,
			title: '',
			perPageWaiter: 20,
			genres: [],
			allGenres: [],
		}
		
		if (!(this.props.location.genre === undefined)) {
			this.state.genres.push(this.props.location.genre)
		}

		this.updatePage = this.updatePage.bind(this)

		this.updateGenre = this.updateGenre.bind(this)
		this.handleGenreAdd = this.handleGenreAdd.bind(this)
		this.resetGenres = this.resetGenres.bind(this)
      this.removeGenre = this.removeGenre.bind(this)

		this.updateTitle = this.updateTitle.bind(this)
		this.handleTitleTextChange = this.handleTitleTextChange.bind(this)
		this.resetTitle = this.resetTitle.bind(this)

		this.onPerPageApply = this.onPerPageApply.bind(this)
		this.handlePerPageChange = this.handlePerPageChange.bind(this)
	}

	componentDidMount() {
		GenresService.getAll()
			.then(res => {
				this.setState({
					allGenres: res.sort((e1, e2) => {
                  return e1.name >= e2.name ? 1 : -1
               }),
				})
            this.updateData(1)
			})
			.catch(err => {
				console.log(err)
			})
		
	}

	updateData(page) {
		MovieService.getMoviesForQuery(
			page,
			this.state.perPage,
			this.state.title,
			this.state.genres
		)
			.then(result => {
				this.setState({
					isLoaded: true,
					movies: result.movies,
					pageAmount: result.pages
				})
			})
			.catch(err => {
				this.setState({
					isLoaded: true,
					error: err,
				})
			})
	}

	updatePage(event) {
		if (this.state.page != event.target.value) {
			this.state.page = event.target.value
			this.updateData(this.state.page)
		}
	}

	updateGenre(event) {
		if (this.state.genres.length == 0)
			this.state.genres.push(this.state.allGenres[0])
		this.updateData(1)
	}

	updateTitle(event) {
		if (this.state.title != event.target.value) {
			this.updateData(1)
		}
	}

	handleGenreAdd(event) {
		this.state.genres.push(
			this.state.allGenres.find(el => el.id == event.target.value)
		)
	}

	handleTitleTextChange(event) {
		this.state.title = event.target.value
	}

	resetGenres(event) {
		if (this.state.genres?.length != 0) {
			this.state.genres = []
			document.getElementById(
				'genreField'
			).value = this.state.allGenres[0].id
			this.updateData(1)
		}
	}

	resetTitle(event) {
		if (this.state.title != '') {
			this.state.title = ''
			document.getElementById('titleField').value = ''
			this.updateData(1)
		}
	}

	onPerPageApply(event) {
		if (this.state.perPageWaiter > 0 && this.state.perPageWaiter < 100) {
			this.state.perPage = this.state.perPageWaiter
			this.updateData(1)
		}
	}

	handlePerPageChange(event) {
		if (event.target.value > 0 && event.target.value <= 100)
			this.state.perPageWaiter = event.target.value
	}

	removeGenre(event){
      this.state.genres = this.state.genres.filter(el => el.id!=event.target.value)
      if (this.state.genres?.length == 0)
         document.getElementById(
				'genreField'
			).value = this.state.allGenres[0].id
      this.updateData(1)
   }

	render() {
		const { error, isLoaded, movies } = this.state

		if (error) {
			return <ErrorComponent error={error} />
		} else if (!isLoaded) {
			return <Loading />
		} else {
			let pagebuttons = []

			for (let i = 1; i <= this.state.pageAmount; i++) {
				pagebuttons.push(
					<label className='page_button__item'>
						<input
							type='radio'
							name='page'
							className='page_input__item'
							key={i}
							onClick={this.updatePage}
							checked={i == this.state.page}
							value={i}
						/>
						<div className='page_label'>{i}</div>
					</label>
				)
			}
			return (
				<div>
					<div className='search_wrap__item'>
						<input
							className='movies_input__item'
							type='text'
							placeholder='Title...'
							id='titleField'
							onChange={this.handleTitleTextChange}
						/>
						<button
							className='serch_button__item'
							onClick={this.updateTitle}
						>
							Search
						</button>
						<button
							className='reset_button__item'
							onClick={this.resetTitle}
						>
							Reset
						</button>
					</div>
					<div className='search_wrap__item'>
						<select
							className='movies_input__item'
							id='genreField'
							onChange={this.handleGenreAdd}
						>
							{this.state.allGenres.map(el => {
								return <option value={el.id}>{el.name}</option>
							})}
						</select>
						<button
							className='serch_button__item'
							onClick={this.updateGenre}
						>
							Search
						</button>
						<button
							className='reset_button__item'
							onClick={this.resetGenres}
						>
							Reset
						</button>
						<br />
						<div className='d-flex align-items-center flex-column genres__continer'>
							{this.state.genres.map(el => {
								return (
									<div className='p-2 genre__item'>
										{el.name}{' '}
										<button
											onClick={this.removeGenre}
											value={el.id}
											className='genre__btn'
										>
											x
										</button>
									</div>
								)
							})}
						</div>
					</div>
					<div className='perPageForm col-md-12'>
						<span>Items per page: </span>
						<input
							className='movies_input__item'
							type='number'
							min='1'
							max='100'
							onChange={this.handlePerPageChange}
							defaultValue='20'
						/>
						<button
							className='serch_button__item'
							onClick={this.onPerPageApply}
						>
							Apply
						</button>
					</div>
					<PageResult movies={movies} className='row' />
					<div>{pagebuttons}</div>
				</div>
			)
		}
	}
}

export default MoviesListPage
