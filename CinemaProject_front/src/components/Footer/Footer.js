import React from 'react'
import './Footer.css';

var phantom = {
  display: 'block',
  padding: '20px',
  width: '100%',
}

function Footer() {
    return (
            <div className="main-footer">
                <div style={phantom} fixed="bottom"/>
                <div className="container-style">
                  <div className="container">
                    <div className="row">
                      {/* Column1 */}
                      <div className="col">
                        <h4>About Us</h4>
                        <ul className="list-unstyled">
                          <a href=""><li>Story</li></a>
                          <a href=""><li>Clients</li></a>
                          <a href=""><li>Testimonials</li></a>
                        </ul>
                      </div>
                      {/* Column2 */}
                      <div className="col">
                        <h4>Service</h4>
                        <ul className="list-unstyled">
                        <a href=""><li>Development</li></a>
                        <a href=""><li>Consulting</li></a>
                        <a href=""><li>Help</li></a>
                        </ul>
                      </div>
                      {/* Column3 */}
                      <div className="col">
                        <h4>Contact Us</h4>
                        <ul className="list-unstyled">
                        <a href=""> <li>Ukraine</li></a>
                        <a href=""><li>United State</li></a>
                        <a href=""> <li>Australia</li></a>
                        </ul>
                      </div>
                      {/* Column4 */}
                      <div className="col">
                        <h4>Social</h4>
                        <ul className="list-unstyled">
                        <a href=""> <li>Facebook</li></a>
                        <a href=""> <li>Instagram</li></a>
                        <a href=""> <li>Youtube</li></a>
                        </ul>
                      </div>
                    </div>
                    <hr />
                    <div className="footer_bottom__item">
                      <p className="col-sm">
                        &copy;{new Date().getFullYear()} Cinema Project | All rights reserved
                      </p>
                    </div>
                  </div>
                </div>
            </div>
    )
}

export default Footer;