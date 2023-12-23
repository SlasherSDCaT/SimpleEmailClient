import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MainPage from "./components/mainPage/MainPage";
import Login from "./components/login/Login";

function App() {
  return (
      <BrowserRouter className="App">
        <Routes>
          <Route path='/' element={<MainPage/>}/>
          <Route path='/login' element={<Login/>}/>
        </Routes>
      </BrowserRouter>
  );
}

export default App;
