package Perun::beans::User;

use strict;
use warnings;

use Perun::Common;
use JSON::PP;

sub new
{
	bless({});
}

sub fromHash
{
	return Perun::Common::fromHash(@_);
}

sub TO_JSON
{
	my $self = shift;

	my $id;
	if (defined($self->{_id})) {
		$id = $self->{_id} * 1;
	} else {
		$id = 0;
	}

	my $firstName;
	if (defined($self->{_firstName})) {
		$firstName = "$self->{_firstName}";
	} else {
		$firstName = undef;
	}

	my $lastName;
	if (defined($self->{_lastName})) {
		$lastName = "$self->{_lastName}";
	} else {
		$lastName = undef;
	}

	my $middleName;
	if (defined($self->{_middleName})) {
		$middleName = "$self->{_middleName}";
	} else {
		$middleName = undef;
	}

	my $titleBefore;
	if (defined($self->{_titleBefore})) {
		$titleBefore = "$self->{_titleBefore}";
	} else {
		$titleBefore = undef;
	}

	my $titleAfter;
	if (defined($self->{_titleAfter})) {
		$titleAfter = "$self->{_titleAfter}";
	} else {
		$titleAfter = undef;
	}

	my $serviceUser;
	if (defined($self->{_serviceUser})) {
		$serviceUser = $self->{_serviceUser};
	} else {
		$serviceUser = undef;
	}

	my $sponsoredUser;
	if (defined($self->{_sponsoredUser})) {
		$sponsoredUser = $self->{_sponsoredUser};
	} else {
		$sponsoredUser = undef;
	}

	return { id         => $id, firstName => $firstName, lastName => $lastName, middleName => $middleName,
		titleBefore     => $titleBefore, titleAfter => $titleAfter, serviceUser => $serviceUser,
		sponsoredUser => $sponsoredUser };
}

sub getId
{
	my $self = shift;

	return $self->{_id};
}

sub setId
{
	my $self = shift;
	$self->{_id} = shift;

	return;
}

sub getTitleBefore
{
	my $self = shift;

	return $self->{_titleBefore};
}

sub setTitleBefore
{
	my $self = shift;
	$self->{_titleBefore} = shift;

	return;
}

sub getFirstName
{
	my $self = shift;

	return $self->{_firstName};
}

sub setFirstName
{
	my $self = shift;
	$self->{_firstName} = shift;

	return;
}

sub getMiddleName
{
	my $self = shift;

	return $self->{_middleName};
}

sub setMiddleName
{
	my $self = shift;
	$self->{_middleName} = shift;

	return;
}

sub getLastName
{
	my $self = shift;

	return $self->{_lastName};
}

sub setLastName
{
	my $self = shift;
	$self->{_lastName} = shift;

	return;
}

sub getTitleAfter
{
	my $self = shift;

	return $self->{_titleAfter};
}

sub setTitleAfter
{
	my $self = shift;
	$self->{_titleAfter} = shift;

	return;
}

sub isServiceUser
{
	my $self = shift;
	return ($self->{_serviceUser}) ? 'true' : 'false';
}

sub setServiceUser
{
	my $self = shift;
	my $value = shift;
	if (ref $value eq "JSON::PP::Boolean")
	{
		$self->{_serviceUser} = $value;
	} elsif ($value eq 'true' || $value eq 1)
	{
		$self->{_serviceUser} = JSON::PP::true;
	} else
	{
		$self->{_serviceUser} = JSON::PP::false;
	}

	return;
}

sub isSponsoredUser
{
	my $self = shift;

	return ($self->{_sponsoredUser}) ? 'true' : 'false';
}

sub setSponsoredUser
{
	my $self = shift;
	my $value = shift;
	if (ref $value eq "JSON::PP::Boolean")
	{
		$self->{_sponsoredUser} = $value;
	} elsif ($value eq 'true' || $value eq 1)
	{
		$self->{_sponsoredUser} = JSON::PP::true;
	} else
	{
		$self->{_sponsoredUser} = JSON::PP::false;
	}

	return;
}

sub getCommonName
{
	my $self = shift;

	return ($self->{_firstName}.' '.(defined $self->{_middleName} ? $self->{_middleName}.' ' : '').$self->{_lastName});
}

sub getDisplayName
{
	my $self = shift;

	return (($self->{_titleBefore} ? $self->{_titleBefore}.' ' : "").($self->{_firstName} ? $self->{_firstName}.' ' : "").($self->{_middleName} ? $self->{_middleName}.' ' : "").($self->{_lastName} ? $self->{_lastName}.' ' : "").($self->{_titleAfter} ? $self->{_titleAfter} : ""));
}

# used only for sorting purpose: LastName FirstName MiddleName
sub getSortingName {
	my $self = shift;
	return (($self->{_lastName} ? $self->{_lastName}.' ' : "").($self->{_firstName} ? $self->{_firstName}.' ' : "").($self->{_middleName} ? $self->{_middleName}.' ' : ""));
}

sub getCommonArrayRepresentation {
	my $user = shift;
	return ($user->getId, $user->getDisplayName, $user->isServiceUser, $user->isSponsoredUser);
}

sub getCommonArrayRepresentationHeading {
	return ('Id', 'Name', 'ServiceUser', 'SponsoredUser');
}


1;
