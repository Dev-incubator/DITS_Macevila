const resultTableBody = document.getElementById('resultTableBody');
const dataContainer = document.getElementById('dataContainer');
let isReverseTest = false;

dataContainer.addEventListener('click', (event) => {
    const {target} = event;
    if (target.closest('#sortTestsButton')) {
        isReverseTest = !isReverseTest;
        reverseTests(target);
    }
});

function reverseTests(target) {
    target.closest('#sortTestsButton').classList.toggle('reverse');
    const reversedTests = Array.from(resultTableBody.querySelectorAll('.test')).reverse();
    resultTableBody.innerHTML = '';
    reversedTests.forEach(test => {
        resultTableBody.append(test);
    })
}