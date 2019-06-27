const today = new Date().toISOString().slice(0, 10); /* YYYY-MM-DD */
const promises = [];
const urls = [];

const results = [];
const jsonRetrieved = [];


const params = (new URL(document.location)).searchParams;
const daySpecified = params.get('day');

const day = daySpecified === null ? today : daySpecified;

for (var hours = 0; hours < 24; hours++) {
    const prefix = (hours < 10) ? '0' : '';
    const url = day + '-' + prefix + hours.toString() + '.json'
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
            jsonRetrieved.push(url);
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
  

    const hoursText = createNode('span');
    const hoursRetrieved = jsonRetrieved
        .map(url => url.slice(11, -5)) /* remove YYYY-MM-DD- and .json*/
        .map(hour => Number(hour) + 1 ) /* convert from UTC to London times */
        .sort((a, b) => (a - b));

    hoursText.innerHTML = `${hoursRetrieved.length} hours retrieved: ${hoursRetrieved.join(' ')}`; 
    append(document.body, hoursText);
    
    const totalSharesText = createNode('p');
    const totalShares = sorted.reduce((a, b) => a + b[1], 0);
    totalSharesText.innerHTML = `Shared today: ${totalShares}`;
    append(document.body, totalSharesText);

    const ul = createNode('ul');
    append(document.body, ul);

    sorted.forEach((share) => {
        const li = createNode('li');
        li.innerHTML = `${share[1]} - ${share[0]}`;
        append(ul, li);
    });

})