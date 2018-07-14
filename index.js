
const today = new Date().toISOString().slice(0, 10) /* YYYY-MM-DD */
const promises = [];
const urls = [];
const results = [];

for (var hours = 0; hours < 25; hours++) {
    const prefix = (hours < 10) ? '0' : '';
    const url = today + '-' + prefix + hours.toString() + '.json'
    urls.push(url) 
}

urls.forEach((url) => {
  promises.push(
    fetch(url).then((response) => {
      if (response.ok) {
        return response.json();
      } else {
        return Promise.resolve(undefined);
      }
     }).then((data) => {
        if (data) {
            Array.prototype.push.apply(results, data.Shares);
        }
     }) 
  );
});


const reducer = (newShares, share) => {
    if (newShares[share.path] == undefined) {
        newShares[share.path] = share.total;
    } else {
        newShares[share.path] = newShares[share.path] + share.total
    }
    return newShares;
};



const createNode = (element) => {
    return document.createElement(element);
}

const append = (parent, el) => {
    return parent.appendChild(el); 
}


Promise
  .all(promises) 
  .then(function() {
    
    const aggregated = results.reduce(reducer, {});
    const sorted =  Object.entries(aggregated).sort((a, b) => {return b[1] - a[1] });
  
    
    const ul = createNode('ul');
    append(document.body, ul);

    sorted.forEach((share) => {
        const li = createNode('li');
        li.innerHTML = `${share[1]} - ${share[0]}`;
        append(ul, li);
    });

})  

  