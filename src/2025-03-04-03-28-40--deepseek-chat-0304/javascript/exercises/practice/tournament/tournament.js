export const tournamentTally = (input) => {
  const teams = {};
  
  // Initialize team stats
  const initTeam = (name) => {
    if (!teams[name]) {
      teams[name] = { mp: 0, w: 0, d: 0, l: 0, p: 0 };
    }
  };
  
  // Process each match
  const processMatch = (match) => {
    const [team1, team2, result] = match.split(';');
    
    initTeam(team1);
    initTeam(team2);
    
    teams[team1].mp++;
    teams[team2].mp++;
    
    switch (result) {
      case 'win':
        teams[team1].w++;
        teams[team1].p += 3;
        teams[team2].l++;
        break;
      case 'loss':
        teams[team2].w++;
        teams[team2].p += 3;
        teams[team1].l++;
        break;
      case 'draw':
        teams[team1].d++;
        teams[team1].p += 1;
        teams[team2].d++;
        teams[team2].p += 1;
        break;
    }
  };
  
  // Process all matches
  if (input) {
    input.trim().split('\n').forEach(processMatch);
  }
  
  // Sort teams by points descending, then alphabetically
  const sortedTeams = Object.keys(teams).sort((a, b) => {
    if (teams[b].p !== teams[a].p) {
      return teams[b].p - teams[a].p;
    }
    return a.localeCompare(b);
  });
  
  // Format the table
  const header = 'Team                           | MP |  W |  D |  L |  P';
  const rows = sortedTeams.map(team => {
    const { mp, w, d, l, p } = teams[team];
    return `${team.padEnd(30)} | ${mp.toString().padStart(2)} | ${w.toString().padStart(2)} | ${d.toString().padStart(2)} | ${l.toString().padStart(2)} | ${p.toString().padStart(2)}`;
  });
  
  return [header, ...rows].join('\n');
};
