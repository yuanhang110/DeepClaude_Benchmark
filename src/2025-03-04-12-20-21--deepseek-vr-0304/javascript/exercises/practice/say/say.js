export const say = (n) => {
    if (n < 0 || n > 999999999999) {
        throw new Error('Number must be between 0 and 999,999,999,999.');
    }
    if (n === 0) {
        return 'zero';
    }

    const ones = ['','one','two','three','four','five','six','seven','eight','nine',
        'ten','eleven','twelve','thirteen','fourteen','fifteen','sixteen','seventeen',
        'eighteen','nineteen'];
    const tens = ['', '', 'twenty','thirty','forty','fifty','sixty','seventy',
        'eighty','ninety'];
    const scales = ['', 'thousand', 'million', 'billion', 'trillion'];

    const convertLessThanOneHundred = (num) => {
        if (num < 20) {
            return ones[num];
        }
        const ten = tens[Math.floor(num / 10)];
        const one = ones[num % 10];
        return one ? `${ten}-${one}` : ten;
    };

    const convertLessThanOneThousand = (num) => {
        if (num === 0) return '';
        const hundreds = Math.floor(num / 100);
        const remainder = num % 100;
        const hundredStr = hundreds > 0 ? `${ones[hundreds]} hundred` : '';
        const remainderStr = convertLessThanOneHundred(remainder);
        if (hundredStr && remainderStr) {
            return `${hundredStr} ${remainderStr}`;
        }
        return hundredStr || remainderStr;
    };

    const chunks = [];
    let current = n;
    while (current > 0) {
        chunks.push(current % 1000);
        current = Math.floor(current / 1000);
    }
    chunks.reverse();

    let result = '';
    for (let i = 0; i < chunks.length; i++) {
        const chunk = chunks[i];
        const chunkWords = convertLessThanOneThousand(chunk);
        if (!chunkWords) continue;

        const scaleIndex = chunks.length - 1 - i;
        const scaleWord = scales[scaleIndex];
        if (scaleWord) {
            result += `${chunkWords} ${scaleWord} `;
        } else {
            result += `${chunkWords} `;
        }
    }

    return result.trim().replace(/  +/g, ' ');
};
