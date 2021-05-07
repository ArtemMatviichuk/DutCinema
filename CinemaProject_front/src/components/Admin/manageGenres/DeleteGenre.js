import React from "react";
import GenreService from "../../../services/GenreService";
import './ManageGenres.css'

class DeleteGenre extends React.Component {
    constructor(props) {
        super(props);
        this.state={
            id: this.props.id
        }
    }
    delete = async(event)=> {
        event.preventDefault();
        await GenreService.deleteGenre(this.state.id)
            .then((response) => {
                window.location.replace("/admin/genres");
            });
    };
    render() {
        return (
            <div  className="genre_delete__item" onClick={this.delete}>
            Delete
            </div>
        )
    }
}
export default DeleteGenre;