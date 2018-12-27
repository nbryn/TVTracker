import React, { Component } from "react";
import VideoProdItem from "../../components/videoProduction/VideoProdItem";
import NewVideoProdButton from "../videoProduction/NewVideoProdButton";
import { connect } from "react-redux";
import { getSeries } from "../../actions/SeriesActions";
import PropTypes from "prop-types";

class SeriesMain extends Component {
  componentDidMount() {
    this.props.getSeries();
  }

  render() {
    const { series } = this.props.series;
    const buttonText = "Add Series";

    return (
      <div className="background">
        <div className="container">
          <div className="row">
            <div className="col-md-12">
              <h2 className="diplay-4">Series</h2>
              <br />
              <NewVideoProdButton text={buttonText} />
              <br />
              <hr align="left" width="57%" />
              {series.map(videoProd => (
                <VideoProdItem key={videoProd.id} videoProd={videoProd} />
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

SeriesMain.propTypes = {
  series: PropTypes.object.isRequired,
  getSeries: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  return { series: state.series };
};

export default connect(
  mapStateToProps,
  { getSeries }
)(SeriesMain);
