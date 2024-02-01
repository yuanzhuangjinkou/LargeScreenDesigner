import requests from "./axios";  //引入二次封装的axios


export const getNumber = (params)=>requests({url:'/user/getNumber ',method:'post',data:params});
