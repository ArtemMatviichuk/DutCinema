import './App.css';
import React from "react";
import {Route, Switch} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Movie from './components/moviePage/Movie';
import Booking from './components/booking/Booking';
import RegistrationForm from "./components/accountLogs/RegistrationForm";
import LoginForm from "./components/accountLogs/LoginForm";
import Contacts from "./components/contacts/Contacts";
import Account from "./components/Account/Account"

import Home from './components/Home/Home'
import Error from './components/error/ErrorComponent'
import MoviesListPage from './components/movies/MoviesListPage';

import './style.scss'
import SessionsMain from './components/Admin/Sessions/SessionsMain';
import EditSession from './components/Admin/Sessions/EditSession';
import CreateSession from './components/Admin/Sessions/CreateSession';
import AllGenres from "./components/Admin/manageGenres/AllGenres"

import UsersMain from './components/Admin/Users/UsersMain';
import AdminMain from './components/Admin/AdminMain';

import AllMovies from "./components/Admin/manageMovies/AllMovies";
import CreateMovie from "./components/Admin/manageMovies/CreateMovie";
import UpdateMovie from "./components/Admin/manageMovies/UpdateMovie";
import ReviewsMain from './components/Admin/Reviews/ReviewsMain';

import AllHalls from './components/Admin/manageHalls/AllHalls';
import CreateHall from './components/Admin/manageHalls/CreateHall';
import UpdateHall from './components/Admin/manageHalls/UpdateHall';

function App() {
  return (
    <div className="App">
        <Header />
        <Switch>
            <Route exact path="/" component={Home} />
            <Route exact path="/contacts" component={Contacts} />

            <Route exact path='/login' component={LoginForm}/>
            <Route exact path='/registration' component={RegistrationForm}/>

            <Route exact path="/movies/:movieId" component={Movie} />
            <Route exact path="/movies" component={MoviesListPage} />

            <Route exact path="/book/:sessionId" component={Booking} />

            <Route exact path="/account" component={Account} />

            <Route exact path="/admin/sessions/create" component={CreateSession} />
            <Route exact path="/admin/sessions/:sessionId" component={EditSession} />
            <Route exact path="/admin/sessions" component={SessionsMain} />

            <Route exact path="/admin/genres" component={AllGenres} />
            
            <Route exact path="/admin/all-movies" component={AllMovies} />
            <Route exact path="/admin/create-movie/" component={CreateMovie} />
            <Route exact path="/admin/update-movie/:movieId" component={UpdateMovie} />

            <Route exact path="/admin/users" component={UsersMain} />

            <Route exact path="/admin/reviews" component={ReviewsMain} />

            <Route exact path="/admin/halls" component={AllHalls} />
            <Route exact path="/admin/create-hall/" component={CreateHall} />
            <Route exact path="/admin/update-hall/:hallId" component={UpdateHall} />

            <Route exact path='/admin' component={AdminMain} />

            <Route component={Error} exact/>
        </Switch>
        <Footer />
    </div>
  );
}

export default App;
